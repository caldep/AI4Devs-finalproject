import React, { useContext, useEffect, useState } from 'react';
import { useIntl } from 'react-intl';
import styled from 'styled-components';
import { Button, Space } from 'antd';
import { AppContext } from '../context/AppContext';
import { fetchSensorData } from '../services/api';
import EquipmentSelector from './EquipmentSelector';
import DateRangePicker from './DateRangePicker';
import SensorChart from './SensorChart';

const DashboardContainer = styled.div`
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
`;

const FilterContainer = styled.div`
  display: flex;
  gap: 20px;
  align-items: flex-end;
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
      <h1>{intl.formatMessage({ id: 'dashboard.title' })}</h1>
      <FilterContainer>
        <EquipmentSelector />
        <DateRangePicker />
        <Button onClick={fetchData} type="primary">
          {intl.formatMessage({ id: 'dashboard.update' })}
        </Button>
      </FilterContainer>
      {Object.keys(sensorData[0] || {}).filter(key => key !== 'timestamp').map((sensorType) => (
        <SensorChart key={sensorType} data={sensorData} sensorType={sensorType} />
      ))}
    </DashboardContainer>
  );
};

export default Dashboard;
