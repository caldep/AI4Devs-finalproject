import React from 'react';
import styled from 'styled-components';
import { useIntl } from 'react-intl';

const LegendContainer = styled.div`
  position: static;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 10px;
  border-top: 1px solid #ccc;
  width: 100%;
  background-color: white;
`;

const LegendTitle = styled.div`
  font-size: 12px;
  margin-right: 10px;
`;

const LegendItemsContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  margin-right: 10px;
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
        <LegendItem color="orange">{intl.formatMessage({ id: 'legend.low' })}</LegendItem>
        <LegendItem color="#333">{intl.formatMessage({ id: 'legend.normal' })}</LegendItem>
        <LegendItem color="red">{intl.formatMessage({ id: 'legend.high' })}</LegendItem>
      </LegendItemsContainer>
    </LegendContainer>
  );
};

export default Legend;
