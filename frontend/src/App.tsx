import React from 'react';
import { IntlProvider } from 'react-intl';
import AppRoutes from './routes/AppRoutes';
import { AppProvider } from './context/AppContext';
import messages from './i18n/messages';

const App: React.FC = () => {
  return (
    <IntlProvider messages={messages.es} locale="es" defaultLocale="es">
      <AppProvider>
        <AppRoutes />
      </AppProvider>
    </IntlProvider>
  );
};

export default App;
