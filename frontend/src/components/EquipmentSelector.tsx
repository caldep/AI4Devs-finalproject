import React, { useContext, useEffect, useState, useCallback } from 'react';
import { Select, message } from 'antd';
import { useIntl } from 'react-intl';
import { AppContext } from '../context/AppContext';
import { fetchEquipments } from '../services/api'; // Asumimos que existe esta función

const { Option } = Select;

const useLoading = (initialState: boolean = false) => {
  const [loading, setLoading] = useState(initialState);
  const getLoading = useCallback(() => loading, [loading]);
  return { loading, setLoading, getLoading };
};

const EquipmentSelector: React.FC<{ size?: 'small' | 'middle' | 'large', style?: React.CSSProperties, onEquipmentChange?: () => void }> = ({ size = 'middle', style, onEquipmentChange }) => {
  const { state, dispatch } = useContext(AppContext);
  const intl = useIntl();
  const { loading, setLoading, getLoading } = useLoading(false);

  const selectedEquipmentOnlyFirstTime = useCallback((equipments: { equipmentId: string; name: string }[]) => {
    const previousSelectedEquipment = state.selectedEquipment;
    if (equipments.length <= 0 || previousSelectedEquipment) {
      return;
    }
    
    const firstEquipment = equipments[0];
    dispatch({ type: 'SET_SELECTED_EQUIPMENT', payload: firstEquipment.equipmentId });
    
    
  }, [state.selectedEquipment, dispatch, onEquipmentChange]);

  const loadEquipments = useCallback(async () => {
    if (getLoading()) {
      return;
    }
    setLoading(true);
    try {
      const currentEquipmentList = state.equipmentList;
      if (currentEquipmentList.length > 0) {
        return;
      }
      const data = await fetchEquipments();
      
      dispatch({ type: 'SET_EQUIPMENT_LIST', payload: data.equipments });
      selectedEquipmentOnlyFirstTime(data.equipments);
    } catch (error) {
      message.error(intl.formatMessage({ id: 'equipment.loadError' }) + error);
    } finally {
      setLoading(false);
    }
  }, [dispatch, intl, selectedEquipmentOnlyFirstTime, loading, setLoading]);

  useEffect(() => {
    loadEquipments();
  }, [loadEquipments]);

  const handleChange = (value: string) => {
    dispatch({ type: 'SET_SELECTED_EQUIPMENT', payload: value });

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
