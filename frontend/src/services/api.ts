import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL;

interface MeasurementGraphicsDTO {
  id: string;
  equipmentId: string;
  registrationDate: string;
  frequency: number;
  current: number;
  internalPressure: number;
  externalPressure: number;
  internalTemperature: number;
  externalTemperature: number;
  vibrationX: number;
  predictiveEventType: string;
  probability: number;
}

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
  const response = await axios.get(`${API_BASE_URL}prediction-data`, {
    params: { equipmentId, startDate, endDate }
  });
  return response.data;
};

export const fetchEquipmentList = async () => {
  const response = await axios.get(`${API_BASE_URL}equipment`);
  return response.data;
};

export const fetchAlerts = async () => {
  const response = await axios.get(`${API_BASE_URL}alerts`);
  return response.data;
};

export const fetchEquipments = async () => {
  const response = await fetch(`${API_BASE_URL}equipments`);
  if (!response.ok) {
    throw new Error('Failed to fetch equipments');
  }
  return response.json();
};

export const postMeasurement = async (measurement: any): Promise<MeasurementGraphicsDTO> => {
  try {
    const response = await axios.post<MeasurementGraphicsDTO>(`${API_BASE_URL}measurements`, measurement);
    return response.data;
  } catch (error) {
    console.error('Error posting measurement:', error);
    throw error;
  }
};
