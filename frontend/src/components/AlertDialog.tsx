import React, { useContext } from 'react';
import { FormattedMessage } from 'react-intl';
import { AppContext } from '../context/AppContext';

const AlertDialog: React.FC = () => {
  const { state, dispatch } = useContext(AppContext);

  if (!state.alert) return null;

  const handleClose = () => {
    dispatch({ type: 'CLEAR_ALERT' });
  };

  return (
    <div className="alert-dialog">
      <h2>
        <FormattedMessage id="alert.title" defaultMessage="Alerta" />
      </h2>
      <p>{state.alert.message}</p>
      <button onClick={handleClose}>
        <FormattedMessage id="alert.close" defaultMessage="Cerrar" />
      </button>
    </div>
  );
};

export default AlertDialog;
