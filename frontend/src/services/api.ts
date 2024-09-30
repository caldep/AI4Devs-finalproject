import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL;

export interface MeasurementGraphicsDTO {
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

export const fetchMeasurementGraphics = async (equipmentId: string, startDate: string, endDate: string): Promise<MeasurementGraphicsDTO[]> => {
  try {
    const response = await axios.get<MeasurementGraphicsDTO[]>(`${API_BASE_URL}measurements/graphics`, {
      params: { equipmentId, startDate, endDate }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching measurement graphics:', error);
    throw error;
  }
};

export const sendScreenshotToBackend = async (imageData: string, fileName: string) => {
  try {
    // Convertir la cadena base64 a un archivo Blob
    const byteString = atob(imageData.split(',')[1]);
    const mimeString = imageData.split(',')[0].split(':')[1].split(';')[0];
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }
    const blob = new Blob([ab], { type: mimeString });

    // Crear un objeto FormData y agregar el archivo
    const formData = new FormData();
    formData.append('file', blob, fileName);
    formData.append('fileName', fileName);

    const response = await axios.post(`${API_BASE_URL}resources/save-screenshot`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    if (response.status !== 200) {
      throw new Error('Error al guardar la captura de pantalla');
    }

    console.log('Captura de pantalla guardada exitosamente');
    return response.data;
  } catch (error) {
    console.error('Error al guardar la captura de pantalla:', error);
    throw error;
  }
};
