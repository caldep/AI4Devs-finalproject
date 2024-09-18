import React from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { FormattedMessage } from 'react-intl';

interface SensorChartProps {
  data: {
    name: string;
    values: { timestamp: number; value: number }[];
  };
}

const SensorChart: React.FC<SensorChartProps> = ({ data }) => {
  return (
    <div style={{ width: '100%', height: 200, marginBottom: 20 }}>
      <h3>
        <FormattedMessage id={`sensor.${data.name}`} defaultMessage={data.name} />
      </h3>
      <ResponsiveContainer>
        <LineChart data={data.values}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="timestamp" tickFormatter={(timestamp) => new Date(timestamp).toLocaleTimeString()} />
          <YAxis />
          <Tooltip labelFormatter={(label) => new Date(label).toLocaleString()} />
          <Line type="monotone" dataKey="value" stroke="#8884d8" dot={false} />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};

export default SensorChart;
