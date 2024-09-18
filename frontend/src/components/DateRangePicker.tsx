import React, { useContext } from 'react';
import { FormattedMessage } from 'react-intl';
import { AppContext } from '../context/AppContext';

const DateRangePicker: React.FC = () => {
  const { state, dispatch } = useContext(AppContext);

  const handleStartDateChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    dispatch({ type: 'SET_DATE_RANGE', payload: { ...state.dateRange, start: event.target.value } });
  };

  const handleEndDateChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    dispatch({ type: 'SET_DATE_RANGE', payload: { ...state.dateRange, end: event.target.value } });
  };

  return (
    <div>
      <label htmlFor="start-date">
        <FormattedMessage id="date.start" defaultMessage="Fecha de inicio:" />
      </label>
      <input
        type="date"
        id="start-date"
        value={state.dateRange.start}
        onChange={handleStartDateChange}
      />

      <label htmlFor="end-date">
        <FormattedMessage id="date.end" defaultMessage="Fecha de fin:" />
      </label>
      <input
        type="date"
        id="end-date"
        value={state.dateRange.end}
        onChange={handleEndDateChange}
      />
    </div>
  );
};

export default DateRangePicker;
