import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UseractivationComponent } from './useractivation.component';

describe('UseractivationComponent', () => {
  let component: UseractivationComponent;
  let fixture: ComponentFixture<UseractivationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UseractivationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UseractivationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
