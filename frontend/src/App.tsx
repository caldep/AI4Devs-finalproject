import React from 'react';
import { IntlProvider } from 'react-intl';
import { ThemeProvider } from 'styled-components';
import AppRoutes from './routes/AppRoutes';
import GlobalStyles from './styles/GlobalStyles';
import { translatedMessages } from './i18n/messages';

const theme = {
  // Aquí puedes definir tu tema
};

const App: React.FC = () => {
  return (
    <IntlProvider messages={translatedMessages} locale="es">
      <ThemeProvider theme={theme}>
        <GlobalStyles />
        <AppRoutes />
      </ThemeProvider>
    </IntlProvider>
  );
};

export default App;
