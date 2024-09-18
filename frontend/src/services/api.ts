import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';

export const fetchSensorData = async (equipmentId: string, startDate: string, endDate: string) => {
  const response = await axios.get(`${API_BASE_URL}/sensor-data`, {
    params: { equipmentId, startDate, endDate }
  });
  return response.data;
};

export const fetchPredictionData = async (equipmentId: string, startDate: string, endDate: string) => {
  const response = await axios.get(`${API_BASE_URL}/prediction-data`, {
    params: { equipmentId, startDate, endDate }
  });
  return response.data;
};

export const fetchEquipmentList = async () => {
  const response = await axios.get(`${API_BASE_URL}/equipment`);
  return response.data;
};
