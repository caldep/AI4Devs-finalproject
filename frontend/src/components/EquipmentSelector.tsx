import React, { useContext } from 'react';
import { Select } from 'antd';
import { useIntl } from 'react-intl';
import { AppContext } from '../context/AppContext';

const { Option } = Select;

const EquipmentSelector: React.FC = () => {
  const { state, dispatch } = useContext(AppContext);
  const intl = useIntl();

  const handleChange = (value: string) => {
    dispatch({ type: 'SET_SELECTED_EQUIPMENT', payload: value });
  };

  return (
    <Select
      style={{ width: 200 }}
      placeholder={intl.formatMessage({ id: 'equipment.select' })}
      onChange={handleChange}
      value={state.selectedEquipment}
    >
      {state.equipmentList.map((equipment) => (
        <Option key={equipment.id} value={equipment.id}>
          {equipment.name}
        </Option>
      ))}
    </Select>
  );
};

export default EquipmentSelector;
