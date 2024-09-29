import React, { useEffect, useState } from 'react';
import styled, { keyframes } from 'styled-components';
import { CheckCircleOutlined, WarningOutlined, CloseCircleOutlined, QuestionCircleOutlined } from '@ant-design/icons';

const fadeIn = keyframes`
  from { opacity: 0; }
  to { opacity: 1; }
`;

const fadeOut = keyframes`
  from { opacity: 1; }
  to { opacity: 0; }
`;

const AlertContainer = styled.div`
  position: fixed;
  top: 1px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1000;
  width: 100%;
  display: flex;
  justify-content: center;
`;

interface AlertItemProps {
  isVisible: boolean;
  eventType: number;
}

const getBackgroundColor = (eventType: string) => {
  switch (eventType) {
    case '0': return '#52c41a'; // Verde para normal
    case '1': return '#faad14'; // Amarillo para peligro
    case '2': return '#f5222d'; // Rojo para alto riesgo
    default: return '#8c8c8c'; // Gris para desconocido
  }
};

const getTextColor = (eventType: string) => {
  return eventType === '1' ? '#000000' : '#ffffff';
};

const AlertItem = styled.div<AlertItemProps>`
  background-color: ${props => getBackgroundColor(props.eventType.toString())};
  color: ${props => getTextColor(props.eventType.toString())};
  padding: 10px 20px;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  animation: ${props => props.isVisible ? fadeIn : fadeOut} 0.5s ease-in-out;
  max-width: 80%;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const IconWrapper = styled.span`
  margin-right: 10px;
  font-size: 20px;
`;

interface AlertMessageProps {
  message: { message: string; eventType: number } | null;
}

const AlertMessage: React.FC<AlertMessageProps> = ({ message }) => {
  const [isVisible, setIsVisible] = useState(false);
  const [currentMessage, setCurrentMessage] = useState<{ message: string; eventType: number } | null>(null);

  useEffect(() => {
    if (message) {
      setCurrentMessage(message);
      setIsVisible(true);
    } else {
      setIsVisible(false);
      setTimeout(() => setCurrentMessage(null), 500);
    }
  }, [message]);

  if (!currentMessage) return null;

  const getIcon = (eventType: number) => {
    switch (eventType) {
      case 0: return <CheckCircleOutlined />;
      case 1: return <WarningOutlined />;
      case 2: return <CloseCircleOutlined />;
      default: return <QuestionCircleOutlined />;
    }
  };

  return (
    <AlertContainer>
      <AlertItem isVisible={isVisible} eventType={currentMessage.eventType}>
        <IconWrapper>{getIcon(currentMessage.eventType)}</IconWrapper>
        {currentMessage.message}
      </AlertItem>
    </AlertContainer>
  );
};

export default AlertMessage;
