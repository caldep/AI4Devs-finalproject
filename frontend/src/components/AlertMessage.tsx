import React, { useEffect, useState } from 'react';
import styled, { keyframes } from 'styled-components';

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

const AlertItem = styled.div<{ isVisible: boolean }>`
  background-color: #ff4d4f;
  color: white;
  padding: 10px 10px;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  animation: ${props => props.isVisible ? fadeIn : fadeOut} 0.5s ease-in-out;
  max-width: 80%;
  text-align: center;
`;

interface AlertMessageProps {
  message: string | null;
}

const AlertMessage: React.FC<AlertMessageProps> = ({ message }) => {
  const [isVisible, setIsVisible] = useState(false);
  const [currentMessage, setCurrentMessage] = useState<string | null>(null);

  useEffect(() => {
    if (message) {
      setCurrentMessage(message);
      setIsVisible(true);
    } else {
      setIsVisible(false);
      setTimeout(() => setCurrentMessage(null), 500); // Espera a que termine la animación de fadeOut
    }
  }, [message]);

  if (!currentMessage) return null;

  return (
    <AlertContainer>
      <AlertItem isVisible={isVisible}>
        {currentMessage}
      </AlertItem>
    </AlertContainer>
  );
};

export default AlertMessage;
