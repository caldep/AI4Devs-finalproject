import React from 'react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import styled from 'styled-components';
import { useIntl } from 'react-intl';

const ChartContainer = styled.div`
  height: 300px;
  width: 100%;
  margin-bottom: 20px;
`;

interface PredictionChartProps {
  data: {
    timestamp: string;
    probability: number;
  }[];
}

const PredictionChart: React.FC<PredictionChartProps> = ({ data }) => {
  const intl = useIntl();

  return (
    <ChartContainer>
      <h3>{intl.formatMessage({ id: 'prediction.title' })}</h3>
      <ResponsiveContainer width="100%" height="100%">
        <AreaChart data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="timestamp" />
          <YAxis />
          <Tooltip />
          <Area type="monotone" dataKey="probability" stroke="#82ca9d" fill="#82ca9d" />
        </AreaChart>
      </ResponsiveContainer>
    </ChartContainer>
  );
};

export default PredictionChart;
