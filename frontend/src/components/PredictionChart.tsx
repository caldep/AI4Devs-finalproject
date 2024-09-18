import React from 'react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { FormattedMessage } from 'react-intl';

type EventType = string;

interface PredictionChartProps {
  data: {
    timestamp: number;
    probability: number;
    event: EventType;
  }[];
}

const PredictionChart: React.FC<PredictionChartProps> = ({ data }) => {
  return (
    <div style={{ width: '100%', height: 200, marginBottom: 20 }}>
      <h3>
        <FormattedMessage id="prediction.title" defaultMessage="Predicción de Eventos" />
      </h3>
      <ResponsiveContainer>
        <AreaChart data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="timestamp" tickFormatter={(timestamp) => new Date(timestamp).toLocaleTimeString()} />
          <YAxis />
          <Tooltip
            labelFormatter={(label) => new Date(label).toLocaleString()}
            formatter={(value, name) => [`${value}%`, <FormattedMessage id={`prediction.${String(name)}`} defaultMessage={String(name)} />]}
          />
          <Area type="monotone" dataKey="probability" stroke="#82ca9d" fill="#82ca9d" />
        </AreaChart>
      </ResponsiveContainer>
    </div>
  );
};

export default PredictionChart;
