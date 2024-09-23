import React from 'react';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from 'recharts';
import styled from 'styled-components';
import { useIntl } from 'react-intl';

interface ChartContainerProps {
  showXAxis: boolean;
}

const ChartContainer = styled.div<ChartContainerProps>`
  width: 100%;
  height: ${props => props.showXAxis ? '15%' : '13%'};
`;

const ChartTitle = styled.h3`
  font-size: 12px;
  margin: 0;
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
  showXAxis: boolean;
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

const SensorChart: React.FC<SensorChartProps> = ({ data, sensorType, showXAxis }) => {
  const intl = useIntl();

  return (
    <ChartContainer showXAxis={showXAxis}>
      <ChartTitle>{intl.formatMessage({ id: `sensor.${sensorType}` })}</ChartTitle>
      <ResponsiveContainer width="100%" height="95%">
        <LineChart data={data} margin={{ top: 5, right: 10, left: 10, bottom: showXAxis ? 20 : 5 }}>
          <CartesianGrid strokeDasharray="3 3" />
          {showXAxis && <XAxis dataKey="timestamp" />}
          <YAxis />
          <Tooltip />
          <Line
            type="monotone"
            dataKey={sensorType}
            stroke={sensorColors[sensorType as keyof typeof sensorColors]}
            dot={false}
          />
        </LineChart>
      </ResponsiveContainer>
    </ChartContainer>
  );
};

export default SensorChart;
