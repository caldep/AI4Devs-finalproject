import React, { useState } from 'react';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  Brush,
} from 'recharts';
import styled from 'styled-components';
import { useIntl } from 'react-intl';

const ChartContainer = styled.div`
  height: 400px;
  width: 100%;
  margin-bottom: 30px;
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
`;

const ChartTitle = styled.h3`
  font-size: 18px;
  margin-bottom: 15px;
  color: #333;
`;

interface SensorData {
  timestamp: string;
  frequency: number;
  current: number;
  internalTemperature: number;
  externalTemperature: number;
  internalPressure: number;
  externalPressure: number;
  vibration: number;
}

interface SensorChartProps {
  data: SensorData[];
  sensorType: string;
}

const sensorColors = {
  frequency: '#8884d8',
  current: '#82ca9d',
  internalTemperature: '#ffc658',
  externalTemperature: '#ff7300',
  internalPressure: '#0088fe',
  externalPressure: '#00c49f',
  vibration: '#ff8042',
};

const SensorChart: React.FC<SensorChartProps> = ({ data, sensorType }) => {
  const intl = useIntl();
  const [sensorData, setSensorData] = useState<SensorData[]>([]);

  return (
    <ChartContainer>
      <ChartTitle>{intl.formatMessage({ id: `sensor.${sensorType}` })}</ChartTitle>
      <ResponsiveContainer width="100%" height="100%">
        <LineChart data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="timestamp" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Line
            type="monotone"
            dataKey={sensorType}
            stroke={sensorColors[sensorType as keyof typeof sensorColors]}
            dot={false}
            activeDot={{ r: 8 }}
          />
          <Brush dataKey="timestamp" height={30} stroke="#8884d8" />
        </LineChart>
      </ResponsiveContainer>
    </ChartContainer>
  );
};

export default SensorChart;
