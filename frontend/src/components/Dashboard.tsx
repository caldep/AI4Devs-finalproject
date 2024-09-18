import React, { useState, useEffect, useContext } from 'react';
import { useIntl } from 'react-intl';
import styled from 'styled-components';
import { AppContext } from '../context/AppContext';
import EquipmentSelector from './EquipmentSelector';
import DateRangePicker from './DateRangePicker';
import SensorChart from './SensorChart';
import PredictionChart from './PredictionChart';
import AlertDialog from './AlertDialog';
import { fetchSensorData, fetchPredictionData, fetchEquipmentList } from '../services/api';
import { translatedMessages } from '../i18n/messages';

const DashboardContainer = styled.div`
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
`;

const Dashboard: React.FC = () => {
  const { state, dispatch } = useContext(AppContext);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const intl = useIntl();

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const [sensorData, predictionData] = await Promise.all([
          fetchSensorData(state.selectedEquipment, state.dateRange.start, state.dateRange.end),
          fetchPredictionData(state.selectedEquipment, state.dateRange.start, state.dateRange.end)
        ]);
        dispatch({ type: 'SET_DATA', payload: { sensorData, predictionData } });
        setLoading(false);
      } catch (err) {
        setError(translatedMessages['app.error']);
        setLoading(false);
      }
    };

    if (state.selectedEquipment && state.dateRange.start && state.dateRange.end) {
      fetchData();
      const interval = setInterval(fetchData, 7 * 60 * 1000); // 7 minutos
      return () => clearInterval(interval);
    }
  }, [state.selectedEquipment, state.dateRange, dispatch, intl]);

  useEffect(() => {
    const loadEquipmentList = async () => {
      try {
        const equipmentList = await fetchEquipmentList();
        dispatch({ type: 'SET_EQUIPMENT_LIST', payload: equipmentList });
      } catch (err) {
        setError(translatedMessages['app.error']);
      }
    };

    loadEquipmentList();
  }, [dispatch, intl]);

  if (loading) return (
    <div>
      {translatedMessages['app.loading'] }
    </div>
  );
  if (error) return <div>{error}</div>;

  return (
    <DashboardContainer>
      <EquipmentSelector />
      <DateRangePicker />
      {state.sensorData.map((sensor, index) => (
        <SensorChart key={index} data={sensor} />
      ))}
      <PredictionChart data={state.predictionData} />
      <AlertDialog />
    </DashboardContainer>
  );
};

export default Dashboard;
