import React, { useState, useEffect, useContext } from 'react';
import { Modal } from 'antd';
import { useIntl } from 'react-intl';
import { AppContext } from '../context/AppContext';

const AlertDialog: React.FC = () => {
  const [isVisible, setIsVisible] = useState(false);
  const { state } = useContext(AppContext);
  const intl = useIntl();

  useEffect(() => {
    if (state.alert) {
      setIsVisible(true);
    }
  }, [state.alert]);

  const handleOk = () => {
    setIsVisible(false);
  };

  return (
    <Modal
      title={intl.formatMessage({ id: 'alert.title' })}
      visible={isVisible}
      onOk={handleOk}
      onCancel={handleOk}
    >
      <p>{state.alert?.message}</p>
    </Modal>
  );
};

export default AlertDialog;
