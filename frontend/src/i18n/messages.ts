import { defineMessages } from 'react-intl';

const messages = defineMessages({
  loading: {
    id: 'app.loading',
    defaultMessage: 'Cargando...',
  },
  error: {
    id: 'app.error',
    defaultMessage: 'Error al cargar los datos',
  },
  equipmentSelector: {
    id: 'app.equipmentSelector',
    defaultMessage: 'Seleccionar Equipo',
  },
  dateRangeStart: {
    id: 'app.dateRangeStart',
    defaultMessage: 'Fecha de inicio',
  },
  dateRangeEnd: {
    id: 'app.dateRangeEnd',
    defaultMessage: 'Fecha de fin',
  },
});

export const translatedMessages = {
  'app.loading': messages.loading.defaultMessage,
  'app.error': messages.error.defaultMessage,
  'app.equipmentSelector': messages.equipmentSelector.defaultMessage,
  'app.dateRangeStart': messages.dateRangeStart.defaultMessage,
  'app.dateRangeEnd': messages.dateRangeEnd.defaultMessage,
};

export default messages;
