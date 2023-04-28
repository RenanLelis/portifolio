import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserActivationRequestComponent } from './user-activation-request.component';

describe('UserActivationRequestComponent', () => {
  let component: UserActivationRequestComponent;
  let fixture: ComponentFixture<UserActivationRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserActivationRequestComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserActivationRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
