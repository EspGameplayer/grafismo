import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TeamStaffMemberDetailComponent } from './team-staff-member-detail.component';

describe('TeamStaffMember Management Detail Component', () => {
  let comp: TeamStaffMemberDetailComponent;
  let fixture: ComponentFixture<TeamStaffMemberDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TeamStaffMemberDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ teamStaffMember: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TeamStaffMemberDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TeamStaffMemberDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load teamStaffMember on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.teamStaffMember).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
