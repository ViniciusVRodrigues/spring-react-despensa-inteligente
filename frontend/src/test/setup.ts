import { afterEach } from 'vitest';
import { cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';

// Limpa o DOM após cada teste
afterEach(() => {
  cleanup();
});
