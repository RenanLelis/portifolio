import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UseractivationRequestComponent } from './useractivation-request.component';

describe('UseractivationRequestComponent', () => {
  let component: UseractivationRequestComponent;
  let fixture: ComponentFixture<UseractivationRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UseractivationRequestComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UseractivationRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
