import React from 'react';
import styled from 'styled-components';
import { useIntl } from 'react-intl';

const LegendContainer = styled.div`
  position: fixed;
  right: 10px;
  bottom: 10px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  padding: 15px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 100px;
  background-color: white;
  z-index: 1000;
`;

const LegendTitle = styled.div`
  font-size: 12px;
  margin-bottom: 5px;
  text-align: center;
  width: 100%;
`;

const LegendItemsContainer = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
`;

const LegendItem = styled.div<{ color: string }>`
  display: flex;
  align-items: center;
  font-size: 10px;
  &:before {
    content: '';
    display: inline-block;
    width: 10px;
    height: 10px;
    margin-right: 5px;
    background-color: ${props => props.color};
  }
`;

const Legend: React.FC = () => {
  const intl = useIntl();

  return (
    <LegendContainer>
      <LegendTitle>{intl.formatMessage({ id: 'legend.title' })}</LegendTitle>
      <LegendItemsContainer>
        <LegendItem color="orange">Bajo</LegendItem>
        <LegendItem color="#333">Normal</LegendItem>
        <LegendItem color="red">Alto</LegendItem>
      </LegendItemsContainer>
    </LegendContainer>
  );
};

export default Legend;
