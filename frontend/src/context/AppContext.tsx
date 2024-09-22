import React, { createContext, useReducer, Dispatch } from 'react';

interface State {
  selectedEquipment: string;
  dateRange: { start: string; end: string };
  equipmentList: { equipmentId: string; name: string }[];
  sensorData: any[]; // Tipo más específico según la estructura de tus datos
  predictionData: any[]; // Tipo más específico según la estructura de tus datos
  alert: { message: string } | null;
  locale: string;
  messages: Record<string, string>;
}

type Action =
  | { type: 'SET_SELECTED_EQUIPMENT'; payload: string }
  | { type: 'SET_DATE_RANGE'; payload: { start: string; end: string } }
  | { type: 'SET_DATA'; payload: { sensorData: any[]; predictionData: any[] } }
  | { type: 'SET_ALERT'; payload: { message: string } }
  | { type: 'CLEAR_ALERT' }
  | { type: 'SET_LOCALE'; payload: string }
  | { type: 'SET_EQUIPMENT_LIST'; payload: { equipmentId: string; name: string }[] };

const initialState: State = {
  selectedEquipment: '',
  dateRange: { start: '2010-01-01T00:00:00', end: '2024-12-31T23:59:59' },
  equipmentList: [],
  sensorData: [],
  predictionData: [],
  alert: null,
  locale: 'es',
  messages: {},
};

const reducer = (state: State, action: Action): State => {
  switch (action.type) {
    case 'SET_SELECTED_EQUIPMENT':
      return { ...state, selectedEquipment: action.payload };
    case 'SET_DATE_RANGE':
      return { 
        ...state, 
        dateRange: { 
          ...state.dateRange,
          ...action.payload
        }
      };
    case 'SET_DATA':
      return { ...state, sensorData: action.payload.sensorData, predictionData: action.payload.predictionData };
    case 'SET_ALERT':
      return { ...state, alert: action.payload };
    case 'CLEAR_ALERT':
      return { ...state, alert: null };
    case 'SET_LOCALE':
      return { ...state, locale: action.payload };
    case 'SET_EQUIPMENT_LIST':
      return { ...state, equipmentList: action.payload };
    default:
      return state;
  }
};

export const AppContext = createContext<{
  state: State;
  dispatch: Dispatch<Action>;
}>({ state: initialState, dispatch: () => null });

export const AppProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [state, dispatch] = useReducer(reducer, initialState);

  return (
    <AppContext.Provider value={{ state, dispatch }}>
      {children}
    </AppContext.Provider>
  );
};
