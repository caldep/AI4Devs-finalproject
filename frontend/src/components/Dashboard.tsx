import React, { useState, useEffect, useContext } from 'react';
import { useIntl } from 'react-intl';
import styled from 'styled-components';
import { AppContext } from '../context/AppContext';
//import EquipmentSelector from './EquipmentSelector';
//import DateRangePicker from './DateRangePicker';
import SensorChart from './SensorChart';
//import PredictionChart from './PredictionChart';
//import AlertDialog from './AlertDialog';
import { fetchSensorData } from '../services/api';
//import { translatedMessages } from '../i18n/messages';

const DashboardContainer = styled.div`
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
`;

const Dashboard: React.FC = () => {
  const { state, dispatch } = useContext(AppContext);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [sensorData, setSensorData] = useState<any[]>([]); // Añade esta línea
  const intl = useIntl();

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const equipmentId = 'EQUIPO1';
        const startDate = '2020-01-01T00:00:00';
        const endDate = '2025-01-01T00:00:00';
        const rawData = await fetchSensorData(equipmentId, startDate, endDate);
        
        // Asumiendo que rawData es un array de objetos con todas las mediciones
        const formattedData = rawData.map((item: any) => ({
          timestamp: item.registrationDate,
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

    fetchData();
  }, [intl]);

  if (loading) return <div>{intl.formatMessage({ id: 'app.loading' })}</div>;
  if (error) return <div>{error}</div>;

  return (
    <DashboardContainer>
      {Object.keys(sensorData[0] || {}).filter(key => key !== 'timestamp').map((sensorType) => (
        <SensorChart key={sensorType} data={sensorData} sensorType={sensorType} />
      ))}
    </DashboardContainer>
  );
};

export default Dashboard;
