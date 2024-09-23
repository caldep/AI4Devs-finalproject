import React, { useContext, useEffect, useState } from 'react';
import { Select, message } from 'antd';
import { useIntl } from 'react-intl';
import { AppContext } from '../context/AppContext';
import { fetchEquipments } from '../services/api'; // Asumimos que existe esta función

const { Option } = Select;

const EquipmentSelector: React.FC<{ size?: 'small' | 'middle' | 'large', style?: React.CSSProperties }> = ({ size = 'middle', style }) => {
  const { state, dispatch } = useContext(AppContext);
  const intl = useIntl();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadEquipments = async () => {
      setLoading(true);
      try {
        const data = await fetchEquipments();
        dispatch({ type: 'SET_EQUIPMENT_LIST', payload: data.equipments });
      } catch (error) {
        message.error(intl.formatMessage({ id: 'equipment.loadError' }));
      } finally {
        setLoading(false);
      }
    };

    loadEquipments();
  }, [dispatch, intl]);

  const handleChange = (value: string) => {
    dispatch({ type: 'SET_SELECTED_EQUIPMENT', payload: value });
  };

  return (
    <Select
      style={style}
      size={size}
      placeholder={intl.formatMessage({ id: 'equipment.select' })}
      onChange={handleChange}
      value={state.selectedEquipment}
      loading={loading}
    >
      {state.equipmentList.map((equipment) => (
        <Option key={equipment.equipmentId} value={equipment.equipmentId}>
          {equipment.name}
        </Option>
      ))}
    </Select>
  );
};

export default EquipmentSelector;
