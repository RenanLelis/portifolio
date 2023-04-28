import { TestBed } from '@angular/core/testing';

import { LogoutGuardService } from './logout-guard.service';

describe('LogoutGuardService', () => {
  let service: LogoutGuardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LogoutGuardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
