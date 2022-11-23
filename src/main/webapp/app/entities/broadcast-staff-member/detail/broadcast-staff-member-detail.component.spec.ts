import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BroadcastStaffMemberDetailComponent } from './broadcast-staff-member-detail.component';

describe('BroadcastStaffMember Management Detail Component', () => {
  let comp: BroadcastStaffMemberDetailComponent;
  let fixture: ComponentFixture<BroadcastStaffMemberDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BroadcastStaffMemberDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ broadcastStaffMember: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BroadcastStaffMemberDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BroadcastStaffMemberDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load broadcastStaffMember on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.broadcastStaffMember).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
