import React, { useContext, useEffect, useState } from 'react';
import { Select, message, Tooltip } from 'antd';
import { useIntl } from 'react-intl';
import { AppContext } from '../context/AppContext';
import { fetchEquipments } from '../services/api';

const { Option } = Select;

const EquipmentSelector: React.FC<{ size?: 'small' | 'middle' | 'large', style?: React.CSSProperties, onEquipmentChange?: () => void }> = ({ size = 'middle', style, onEquipmentChange }) => {
  const { state, dispatch } = useContext(AppContext);
  const intl = useIntl();
  const [loading, setLoading] = useState(false);
  const [hasAttemptedLoad, setHasAttemptedLoad] = useState(false);

  const selectedEquipmentOnlyFirstTime = (equipments: { equipmentId: string; name: string }[]) => {
    const previousSelectedEquipment = state.selectedEquipment;
    if (equipments.length > 0 && !previousSelectedEquipment) {
      const firstEquipment = equipments[0];
      dispatch({ type: 'SET_SELECTED_EQUIPMENT', payload: firstEquipment.equipmentId });
      if (onEquipmentChange) {
        onEquipmentChange();
      }
    }
  };

  useEffect(() => {
    const loadEquipments = async () => {
      if (hasAttemptedLoad || state.equipmentList.length > 0) {
        return;
      }

      setLoading(true);
      setHasAttemptedLoad(true);

      try {
        const data = await fetchEquipments();
        dispatch({ type: 'SET_EQUIPMENT_LIST', payload: data.equipments });
        selectedEquipmentOnlyFirstTime(data.equipments);
      } catch (error) {
        message.error(intl.formatMessage({ id: 'equipment.loadError' }) + error);
      } finally {
        setLoading(false);
      }
    };

    loadEquipments();
  }, [dispatch, intl, state.equipmentList, hasAttemptedLoad, state.selectedEquipment, onEquipmentChange]);

  const handleChange = (value: string) => {
    dispatch({ type: 'SET_SELECTED_EQUIPMENT', payload: value });
    if (onEquipmentChange) {
      onEquipmentChange();
    }
  };

  return (
    <Tooltip title={intl.formatMessage({ id: 'equipment.selectTooltip' })}>
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
    </Tooltip>
  );
};

export default EquipmentSelector;
