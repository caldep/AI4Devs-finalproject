import React, { useContext } from 'react';
import { FormattedMessage } from 'react-intl';
import { AppContext } from '../context/AppContext';

const EquipmentSelector: React.FC = () => {
  const { state, dispatch } = useContext(AppContext);

  const handleChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    dispatch({ type: 'SET_SELECTED_EQUIPMENT', payload: event.target.value });
  };

  return (
    <div>
      <label htmlFor="equipment-select">
        <FormattedMessage id="equipment.select" defaultMessage="Seleccionar Equipo:" />
      </label>
      <select id="equipment-select" value={state.selectedEquipment} onChange={handleChange}>
        {state.equipmentList.map((equipment) => (
          <option key={equipment.id} value={equipment.id}>
            {equipment.name}
          </option>
        ))}
      </select>
    </div>
  );
};

export default EquipmentSelector;
