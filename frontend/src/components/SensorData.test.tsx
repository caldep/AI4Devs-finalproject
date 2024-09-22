import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import SensorDataTest from './SensorData';
import { fetchSensorData } from '../services/api';

jest.mock('../services/api');

const mockSensorData = [
  {
    equipmentId: 'EQUIPO1',
    registrationDate: '2023-01-01T00:00:00',
    frequency: 50.0,
    current: 10.0,
    internalPressure: 2.5,
    externalPressure: 3.0,
    internalTemperature: 60.0,
    externalTemperature: 65.0,
    vibrationX: 0.5
  }
];

describe('SensorDataTest Component', () => {
  beforeEach(() => {
    (fetchSensorData as jest.Mock).mockClear();
  });

  it('renders loading state initially', () => {
    render(<SensorDataTest />);
    expect(screen.getByText('Cargando...')).toBeInTheDocument();
  });

  it('renders sensor data when fetch is successful', async () => {
    (fetchSensorData as jest.Mock).mockResolvedValue(mockSensorData);

    render(<SensorDataTest />);

    await waitFor(() => {
      expect(screen.getByText(/Datos del Sensor/)).toBeInTheDocument();
    });

    expect(screen.getByText(/EQUIPO1/)).toBeInTheDocument();
    expect(screen.getByText(/2023-01-01T00:00:00/)).toBeInTheDocument();
    expect(screen.getByText(/Frecuencia: 50/)).toBeInTheDocument();
  });

  it('renders error message when fetch fails', async () => {
    (fetchSensorData as jest.Mock).mockRejectedValue(new Error('API Error'));

    render(<SensorDataTest />);

    await waitFor(() => {
      expect(screen.getByText('Error al cargar los datos del sensor')).toBeInTheDocument();
    });
  });
});
