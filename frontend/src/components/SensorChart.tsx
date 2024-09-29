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
import { sensorColors, sensorRanges, statusColors } from '../config/sensorConfig';

interface ChartContainerProps {
  showXAxis: boolean;
}

type SensorType = keyof typeof sensorRanges;

const ChartContainer = styled.div<ChartContainerProps>`
  width: 100%;
  height: ${props => props.showXAxis ? '14%' : '12%'};
  display: flex;
  align-items: center;
`;

const ChartWrapper = styled.div`
  flex: 1;
  height: 100%;
`;

const CurrentValueBox = styled.div<{ color: string }>`
  width: 100px;
  height: 80%;
  margin-left: 10px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border: 1px solid #ccc;
  border-radius: 5px;
`;

const CurrentValueTitle = styled.div`
  font-size: 12px;
  color: #333;
  margin-bottom: 5px;
`;

const CurrentValue = styled.div<{ color: string, isOutOfRange: boolean }>`
  font-size: 24px;
  font-weight: bold;
  color: ${props => props.color};
  ${props => props.isOutOfRange && `
    animation: blink 1s linear infinite;
    @keyframes blink {
      50% { opacity: 0.5; }
    }
  `}
`;

const RangeValue = styled.div`
  font-size: 12px;
  color: #666;
`;

const Legend = styled.div`
  display: flex;
  justify-content: space-around;
  width: 100%;
  margin-top: 5px;
  font-size: 10px;
`;

const LegendItem = styled.div<{ color: string }>`
  display: flex;
  align-items: center;
  &:before {
    content: '';
    display: inline-block;
    width: 10px;
    height: 10px;
    margin-right: 5px;
    background-color: ${props => props.color};
  }
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
  sensorType: SensorType;
  showXAxis: boolean;
}

const ChartTitle = styled.h3`
  font-size: 12px;
  margin-bottom: 10px;
`;

const SensorChart: React.FC<SensorChartProps> = ({ data, sensorType, showXAxis }) => {
  const intl = useIntl();
  const currentValue = data[data.length - 1]?.[sensorType] || 0;
  const isOutOfRange = currentValue < sensorRanges[sensorType].min || currentValue > sensorRanges[sensorType].max;

  const getValueColor = (value: number, min: number, max: number) => {
    if (value < min) return statusColors.low;
    if (value > max) return statusColors.high;
    return statusColors.normal;
  };

  const formatXAxis = (tickItem: string) => {
    const date = new Date(tickItem);
    return date.toISOString().split('T')[0];
  };

  return (
    <ChartContainer showXAxis={showXAxis}>
      <ChartWrapper>
        <ChartTitle>{intl.formatMessage({ id: `sensor.${sensorType}` })}</ChartTitle>
        <ResponsiveContainer width="100%" height="95%">
          <LineChart data={data} margin={{ top: 5, right: 10, left: 10, bottom: showXAxis ? 20 : 5 }}>
            <CartesianGrid strokeDasharray="3 3" />
            {showXAxis && <XAxis dataKey="timestamp" tickFormatter={formatXAxis} />}
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
      </ChartWrapper>
      <CurrentValueBox color={sensorColors[sensorType as keyof typeof sensorColors]}>
        <CurrentValueTitle>
          {intl.formatMessage({ id: `sensor.${sensorType}.current` })}
        </CurrentValueTitle>
        <CurrentValue 
          color={getValueColor(
            currentValue,
            sensorRanges[sensorType].min,
            sensorRanges[sensorType].max
          )}
          isOutOfRange={isOutOfRange}
        >
          {currentValue.toFixed(2)}
        </CurrentValue>
        <RangeValue>
          Obj. [ {sensorRanges[sensorType].min} : {sensorRanges[sensorType].max} ]
        </RangeValue>
      </CurrentValueBox>
    </ChartContainer>
  );
};

export default SensorChart;
