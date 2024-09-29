export const sensorColors = {
  frequency: '#8884d8',
  current: '#82ca9d',
  internalTemperature: '#ffc658',
  externalTemperature: '#ff7300',
  internalPressure: '#0088fe',
  externalPressure: '#00c49f',
  vibration: '#ff8042',
};

export const sensorRanges = {
  frequency: { min: 10, max: 60 },
  current: { min: 10, max: 400 },
  internalPressure: { min: 10, max: 500 },
  externalPressure: { min: 3000, max: 3400 },
  internalTemperature: { min: 10, max: 250 },
  externalTemperature: { min: 10, max: 250 },
  vibration: { min: 0, max: 1 },
};

export const statusColors = {
  low: 'orange',
  high: 'red',
  normal: '#333',
};
