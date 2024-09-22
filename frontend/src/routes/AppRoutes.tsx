import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Dashboard from '../components/Dashboard';
import SensorData from '../components/SensorData';

const AppRoutes: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/sensor-data" element={<SensorData />} />
      </Routes>
    </Router>
  );
};

export default AppRoutes;
