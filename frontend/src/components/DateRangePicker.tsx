import React, { useContext } from 'react';
import { DatePicker, Space, Button, Tooltip } from 'antd';
import type { RangePickerProps } from 'antd/es/date-picker';
import { useIntl } from 'react-intl';
import dayjs from 'dayjs';
import { AppContext } from '../context/AppContext';

const { RangePicker } = DatePicker;

const DateRangePicker: React.FC<{ size?: 'small' | 'middle' | 'large', style?: React.CSSProperties }> = ({ size = 'middle', style }) => {
  const { state, dispatch } = useContext(AppContext);
  const intl = useIntl();

  const handleChange = (dates: RangePickerProps['value'], dateStrings: [string, string]) => {
    if (dates && dates[0] && dates[1]) {
      dispatch({
        type: 'SET_DATE_RANGE',
        payload: { start: dates[0].toISOString(), end: dates[1].toISOString() },
      });
    }
  };

  const setPresetRange = (start: dayjs.Dayjs, end: dayjs.Dayjs) => {
    dispatch({
      type: 'SET_DATE_RANGE',
      payload: { start: start.toISOString(), end: end.toISOString() },
    });
  };

  const startDate = state.dateRange?.start ? dayjs(state.dateRange.start) : dayjs('2024-01-01T00:00:00');
  const endDate = state.dateRange?.end ? dayjs(state.dateRange.end) : dayjs('2024-12-31T23:59:59');

  return (
    <Space direction="vertical" size={12}>
      <Tooltip title={intl.formatMessage({ id: 'date.rangeTooltip' })}>
        <RangePicker
          onChange={handleChange}
          value={[startDate, endDate]}
          placeholder={[
            intl.formatMessage({ id: 'date.start' }),
            intl.formatMessage({ id: 'date.end' }),
          ]}
          style={style}
          size={size}
        />
      </Tooltip>
      <Space>
        <Tooltip title={intl.formatMessage({ id: 'date.thisMonthTooltip' })}>
          <Button onClick={() => setPresetRange(dayjs().startOf('month'), dayjs().endOf('month'))}>
            {intl.formatMessage({ id: 'date.thisMonth' })}
          </Button>
        </Tooltip>
        <Tooltip title={intl.formatMessage({ id: 'date.lastMonthTooltip' })}>
        <Button onClick={() => setPresetRange(dayjs().subtract(1, 'month').startOf('month'), dayjs().subtract(1, 'month').endOf('month'))}>
          {intl.formatMessage({ id: 'date.lastMonth' })}
        </Button>
        </Tooltip>
        {false && (
          <Button onClick={() => setPresetRange(dayjs().startOf('year'), dayjs().endOf('year'))}>
            {intl.formatMessage({ id: 'date.thisYear' })}
          </Button>
        )}
        
      </Space>
    </Space>
  );
};

export default DateRangePicker;
