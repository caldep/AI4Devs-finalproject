import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL;

export const fetchSensorData = async (equipmentId: string, startDate: string, endDate: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}measurements`, {
      params: { equipmentId, startDate, endDate }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching sensor data:', error);
    throw error;
  }
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

export const fetchAlerts = async () => {
  const response = await axios.get(`${API_BASE_URL}/alerts`);
  return response.data;
};
