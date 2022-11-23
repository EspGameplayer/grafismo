import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TeamStaffMemberService } from '../service/team-staff-member.service';
import { ITeamStaffMember, TeamStaffMember } from '../team-staff-member.model';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

import { TeamStaffMemberUpdateComponent } from './team-staff-member-update.component';

describe('TeamStaffMember Management Update Component', () => {
  let comp: TeamStaffMemberUpdateComponent;
  let fixture: ComponentFixture<TeamStaffMemberUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let teamStaffMemberService: TeamStaffMemberService;
  let personService: PersonService;
  let teamService: TeamService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TeamStaffMemberUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TeamStaffMemberUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TeamStaffMemberUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    teamStaffMemberService = TestBed.inject(TeamStaffMemberService);
    personService = TestBed.inject(PersonService);
    teamService = TestBed.inject(TeamService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call person query and add missing value', () => {
      const teamStaffMember: ITeamStaffMember = { id: 456 };
      const person: IPerson = { id: 86024 };
      teamStaffMember.person = person;

      const personCollection: IPerson[] = [{ id: 84384 }];
      jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
      const expectedCollection: IPerson[] = [person, ...personCollection];
      jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ teamStaffMember });
      comp.ngOnInit();

      expect(personService.query).toHaveBeenCalled();
      expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, person);
      expect(comp.peopleCollection).toEqual(expectedCollection);
    });

    it('Should call Team query and add missing value', () => {
      const teamStaffMember: ITeamStaffMember = { id: 456 };
      const team: ITeam = { id: 70196 };
      teamStaffMember.team = team;

      const teamCollection: ITeam[] = [{ id: 59740 }];
      jest.spyOn(teamService, 'query').mockReturnValue(of(new HttpResponse({ body: teamCollection })));
      const additionalTeams = [team];
      const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
      jest.spyOn(teamService, 'addTeamToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ teamStaffMember });
      comp.ngOnInit();

      expect(teamService.query).toHaveBeenCalled();
      expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
      expect(comp.teamsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const teamStaffMember: ITeamStaffMember = { id: 456 };
      const person: IPerson = { id: 18829 };
      teamStaffMember.person = person;
      const team: ITeam = { id: 27049 };
      teamStaffMember.team = team;

      activatedRoute.data = of({ teamStaffMember });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(teamStaffMember));
      expect(comp.peopleCollection).toContain(person);
      expect(comp.teamsSharedCollection).toContain(team);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TeamStaffMember>>();
      const teamStaffMember = { id: 123 };
      jest.spyOn(teamStaffMemberService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ teamStaffMember });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: teamStaffMember }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(teamStaffMemberService.update).toHaveBeenCalledWith(teamStaffMember);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TeamStaffMember>>();
      const teamStaffMember = new TeamStaffMember();
      jest.spyOn(teamStaffMemberService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ teamStaffMember });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: teamStaffMember }));
      saveSubject.complete();

      // THEN
      expect(teamStaffMemberService.create).toHaveBeenCalledWith(teamStaffMember);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TeamStaffMember>>();
      const teamStaffMember = { id: 123 };
      jest.spyOn(teamStaffMemberService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ teamStaffMember });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(teamStaffMemberService.update).toHaveBeenCalledWith(teamStaffMember);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPersonById', () => {
      it('Should return tracked Person primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPersonById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTeamById', () => {
      it('Should return tracked Team primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTeamById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
