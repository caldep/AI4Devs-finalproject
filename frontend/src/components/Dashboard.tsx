import React, { useContext, useEffect, useState } from 'react';
import { useIntl } from 'react-intl';
import styled from 'styled-components';
import { Button, Row, Col } from 'antd';
import { AppContext } from '../context/AppContext';
import { fetchSensorData } from '../services/api';
import EquipmentSelector from './EquipmentSelector';
import DateRangePicker from './DateRangePicker';
import SensorChart from './SensorChart';

type SensorType = 'frequency' | 'current' | 'internalPressure' | 'externalPressure' | 'internalTemperature' | 'externalTemperature' | 'vibration';

const sensorTypes: SensorType[] = [
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
  const intl = useIntl();

  const fetchData = async () => {
    if (!state.selectedEquipment) return;

    try {
      setLoading(true);
      const startDate = state.dateRange.start;
      const endDate = state.dateRange.end;
      const validStartDate = startDate ? startDate.slice(0, 19) : '2024-01-01T00:00:00';
      const validEndDate = endDate ? endDate.slice(0, 19) : '2024-12-31T23:59:59';
      const rawData = await fetchSensorData(state.selectedEquipment, validStartDate, validEndDate);
      
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

  useEffect(() => {
    fetchData();
  }, [state.selectedEquipment]);

  if (loading) return <div>{intl.formatMessage({ id: 'app.loading' })}</div>;
  if (error) return <div>{error}</div>;

  return (
    <DashboardContainer>
      <Header>
        <Title>{intl.formatMessage({ id: 'dashboard.title' })}</Title>
        <Controls>
          <EquipmentSelector size="small" style={{ width: '150px' }} />
          <DateRangePicker size="small" style={{ width: '200px' }} />
          <Button onClick={fetchData} type="primary" size="small">
            {intl.formatMessage({ id: 'dashboard.update' })}
          </Button>
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
      </ChartsContainer>
    </DashboardContainer>
  );
};

export default Dashboard;
