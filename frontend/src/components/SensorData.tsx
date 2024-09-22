import React, { useState, useEffect } from 'react';
import { fetchSensorData } from '../services/api';

const SensorData: React.FC = () => {
  const [sensorData, setSensorData] = useState<any[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadSensorData = async () => {
      try {
        const equipmentId = 'Equipo1';
        const startDate = '2020-01-01T00:00:00';
        const endDate = '2024-12-31T23:59:59';
        const data = await fetchSensorData(equipmentId, startDate, endDate);
        setSensorData(data);
        setLoading(false);
      } catch (err) {
        setError('Error al cargar los datos del sensor');
        setLoading(false);
      }
    };

    loadSensorData();
  }, []);

  if (loading) return <div>Cargando...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div>
      <h2>Datos del Sensor</h2>
      <ul>
        {sensorData.map((measurement, index) => (
          <li key={index}>
            Equipo: {measurement.equipmentId}, 
            Fecha: {measurement.registrationDate}, 
            Frecuencia: {measurement.frequency},
            Corriente: {measurement.current},
            Presión interna: {measurement.internalPressure},
            Presión externa: {measurement.externalPressure},
            Temperatura interna: {measurement.internalTemperature},
            Temperatura externa: {measurement.externalTemperature},
            Vibración: {measurement.vibrationX}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default SensorData;
