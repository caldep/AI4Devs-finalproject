import React, { useContext, useEffect, useState, useCallback } from 'react';
import { Select, message } from 'antd';
import { useIntl } from 'react-intl';
import { AppContext } from '../context/AppContext';
import { fetchEquipments } from '../services/api'; // Asumimos que existe esta función

const { Option } = Select;

const EquipmentSelector: React.FC<{ size?: 'small' | 'middle' | 'large', style?: React.CSSProperties, onEquipmentChange?: () => void }> = ({ size = 'middle', style, onEquipmentChange }) => {
  const { state, dispatch } = useContext(AppContext);
  const intl = useIntl();
  const [loading, setLoading] = useState(false);
  const [isInitialLoad, setIsInitialLoad] = useState(true);

  const loadEquipments = useCallback(async () => {
    setLoading(true);
    try {
      const data = await fetchEquipments();
      dispatch({ type: 'SET_EQUIPMENT_LIST', payload: data.equipments });
      if (data.equipments.length > 0 && isInitialLoad) {
        const firstEquipment = data.equipments[0];
        dispatch({ type: 'SET_SELECTED_EQUIPMENT', payload: firstEquipment.equipmentId });
        if (onEquipmentChange) {
          onEquipmentChange();
        }
        setIsInitialLoad(false);
      }
    } catch (error) {
      message.error(intl.formatMessage({ id: 'equipment.loadError' }) + error);
    } finally {
      setLoading(false);
    }
  }, [dispatch, intl, onEquipmentChange, isInitialLoad]);

  useEffect(() => {
    loadEquipments();
  }, [loadEquipments]);

  const handleChange = (value: string) => {
    dispatch({ type: 'SET_SELECTED_EQUIPMENT', payload: value });
    if (onEquipmentChange) {
      onEquipmentChange();
    }
  };

  return (
    <Select
      style={style}
      size={size}
      placeholder={intl.formatMessage({ id: 'equipment.select' })}
      onChange={handleChange}
      value={state.selectedEquipment || undefined}
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
