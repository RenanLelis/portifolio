import { TestBed } from '@angular/core/testing';

import { ListsTasksService } from './lists-tasks.service';

describe('ListsTasksService', () => {
  let service: ListsTasksService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ListsTasksService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
