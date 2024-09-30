import React, { useContext, useEffect, useState, useCallback } from 'react';
import { useIntl } from 'react-intl';
import styled from 'styled-components';
import { Button, Row, Col } from 'antd';
import { AppContext } from '../context/AppContext';
import { fetchSensorData, postMeasurement, fetchMeasurementGraphics, MeasurementGraphicsDTO, sendScreenshotToBackend } from '../services/api'; // Ajusta la ruta según sea necesario
import EquipmentSelector from './EquipmentSelector';
import DateRangePicker from './DateRangePicker';
import SensorChart from './SensorChart';
import Legend from './Legend';
import { Modal } from 'antd';
import { message } from 'antd';
import AlertMessage from './AlertMessage';
import html2canvas from 'html2canvas';
import { format } from 'date-fns';

type SensorType = 'predictiveEventType' | 'probability' | 'frequency' | 'current' | 'internalPressure' | 'externalPressure' | 'internalTemperature' | 'externalTemperature' | 'vibration';

const sensorTypes: SensorType[] = [
  'predictiveEventType',
  'probability',
  'frequency',
  'current',
  'internalTemperature',
  'externalTemperature',
  'internalPressure',
  'externalPressure',
  'vibration',
];

const DashboardContainer = styled.div`
  height: 100vh;
  display: flex;
  flex-direction: column;
  padding: 10px;
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
`;

const Title = styled.h1`
  margin: 0;
  font-size: 16px;
`;

const Controls = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  align-items: center;
`;

const ChartsContainer = styled.div`
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  gap: 5px;
  overflow: hidden;
`;

const Dashboard: React.FC = () => {
  const { state, dispatch } = useContext(AppContext);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [sensorData, setSensorData] = useState<any[]>([]);
  const [isSimulating, setIsSimulating] = useState(false);
  const [simulationInterval, setSimulationInterval] = useState<NodeJS.Timeout | null>(null);
  const intl = useIntl();
  const [alertMessage, setAlertMessage] = useState<{ message: string; eventType: number } | null>(null);

  const fetchData = async () => {
    if (!state.selectedEquipment) return;

    try {
      setLoading(true);
      const startDate = state.dateRange.start;
      const endDate = state.dateRange.end;
      const validStartDate = startDate ? startDate.slice(0, 19) : '2024-01-01T00:00:00';
      const validEndDate = endDate ? endDate.slice(0, 19) : '2024-12-31T23:59:59';
      const rawData = await fetchMeasurementGraphics(state.selectedEquipment, validStartDate, validEndDate);
      
      const formattedData = rawData.map((item: MeasurementGraphicsDTO) => ({
        timestamp: item.registrationDate,
        predictiveEventType: Number(item.predictiveEventType),
        probability: item.probability * 100, // Convertir a porcentaje
        frequency: item.frequency,
        current: item.current,
        internalTemperature: item.internalTemperature,
        externalTemperature: item.externalTemperature,
        internalPressure: item.internalPressure,
        externalPressure: item.externalPressure,
        vibration: item.vibrationX
      }));

      setSensorData(formattedData);
      setLoading(false);
    } catch (err) {
      setError(intl.formatMessage({ id: 'app.error' }));
      setLoading(false);
    }
  };

  const generateRandomMeasurement = () => {
    return {
      equipmentId: state.selectedEquipment,
      registrationDate: new Date().toISOString(),
      frequency: 40.0 + Math.random() * 20.0,
      current: 250.0 + Math.random() * 80.0,
      internalPressure: 500.0 + Math.random() * 300.0,
      externalPressure: 2000.0 + Math.random() * 1500.0,
      internalTemperature: 70.0 + Math.random() * 200.0,
      externalTemperature: 100.0 + Math.random() * 150.0,
      vibrationX: Math.random()
    };
  };

  const simulateMeasurement = useCallback(async () => {
    if (!state.selectedEquipment) return;

    try {
      const measurement = generateRandomMeasurement();
      const response = await postMeasurement(measurement);
      
      if (Number(response.predictiveEventType) >= 0) {
        const newMessage = intl.formatMessage(
          { id: 'alert.eventMessage' },
          { 
            eventType: response.predictiveEventType,
            probability: (response.probability * 100).toFixed(2)
          }
        );
        setAlertMessage({ message: newMessage, eventType: Number(response.predictiveEventType) });
        captureScreenshot();
        setTimeout(() => setAlertMessage(null), 5000);
      }

      await fetchData();
    } catch (err) {
      console.error('Error en la simulación:', err);
      setAlertMessage({ message: intl.formatMessage({ id: 'simulation.error' }), eventType: 3 });
      setTimeout(() => setAlertMessage(null), 5000);
    }
  }, [state.selectedEquipment, intl, fetchData]);

  const toggleSimulation = () => {
    if (isSimulating) {
      if (simulationInterval) {
        clearInterval(simulationInterval);
      }
      setIsSimulating(false);
      return;
    } 
    if (!state.selectedEquipment) {
      Modal.error({
        title: intl.formatMessage({ id: 'error.noEquipment' }),
        content: intl.formatMessage({ id: 'error.selectEquipment' })
      });
      return;
    }

    const endDate = new Date(state.dateRange.end);
    if (endDate <= new Date()) {
      Modal.error({
        title: intl.formatMessage({ id: 'error.invalidDateRange' }),
        content: intl.formatMessage({ id: 'error.futureEndDate' })
      });
      return;
    }
    simulateMeasurement();
    const interval = setInterval(simulateMeasurement, 7000);
    setSimulationInterval(interval);
    setIsSimulating(true);
    
  };

  const captureScreenshot = () => {
    const element = document.body;
    html2canvas(element).then((canvas) => {
      const image = canvas.toDataURL("image/png");
      const timestamp = format(new Date(), 'yyyyMMddHHmmss');
      const fileName = `${timestamp}-alert.png`;

      sendScreenshotToBackend(image, fileName)
        .catch((error) => {
          message.error(intl.formatMessage({ id: 'screenshot.error' }));
          console.error('Error al guardar la captura de pantalla:', error);
        });
    });
  };

  useEffect(() => {
    fetchData();
  }, [state.selectedEquipment, state.dateRange]);

  useEffect(() => {
    return () => {
      if (simulationInterval) {
        clearInterval(simulationInterval);
      }
    };
  }, [simulationInterval]);

  if (loading) return <div>{intl.formatMessage({ id: 'app.loading' })}</div>;
  if (error) return <div>{error}</div>;

  return (
    <DashboardContainer>
      <Header>
        <Title>{intl.formatMessage({ id: 'dashboard.title' })}</Title>
        <AlertMessage message={alertMessage} />
        <Controls>
          <EquipmentSelector size="small" style={{ width: '150px' }} />
          <DateRangePicker size="small" style={{ width: '210px' }} />
          {/*<Button onClick={fetchData} type="primary" size="small">
            {intl.formatMessage({ id: 'dashboard.update' })}
          </Button>*/}
          <Button onClick={toggleSimulation} type={isSimulating ? "default" : "primary"} size="small">
            {intl.formatMessage({ id: isSimulating ? 'dashboard.stopSimulation' : 'dashboard.startSimulation' })}
          </Button>
          {/*
          <Button onClick={captureScreenshot} size="small">
            {intl.formatMessage({ id: 'dashboard.takeScreenshot' })}
          </Button>
          */}
        </Controls>
      </Header>
      <ChartsContainer>
        {sensorTypes.map((sensorType, index) => (
          <SensorChart
            key={sensorType}
            data={sensorData}
            sensorType={sensorType}
            showXAxis={index === sensorTypes.length - 1}
          />
        ))}
        <Legend />
      </ChartsContainer>
    </DashboardContainer>
  );
};

export default Dashboard;