import React, { useContext } from 'react';
import { DatePicker } from 'antd';
import type { RangePickerProps } from 'antd/es/date-picker';
import { useIntl } from 'react-intl';
import dayjs from 'dayjs';
import { AppContext } from '../context/AppContext';

const { RangePicker } = DatePicker;

const DateRangePicker: React.FC = () => {
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

  // Verificar si state.dateRange existe antes de acceder a sus propiedades
  const startDate = state.dateRange?.start ? dayjs(state.dateRange.start) : null;
  const endDate = state.dateRange?.end ? dayjs(state.dateRange.end) : null;

  return (
    <RangePicker
      onChange={handleChange}
      value={[startDate, endDate]}
      placeholder={[
        intl.formatMessage({ id: 'date.start' }),
        intl.formatMessage({ id: 'date.end' }),
      ]}
    />
  );
};

export default DateRangePicker;
