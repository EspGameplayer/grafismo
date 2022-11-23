import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CallupService } from '../service/callup.service';
import { ICallup, Callup } from '../callup.model';
import { IMatchPlayer } from 'app/entities/match-player/match-player.model';
import { MatchPlayerService } from 'app/entities/match-player/service/match-player.service';
import { ITeamStaffMember } from 'app/entities/team-staff-member/team-staff-member.model';
import { TeamStaffMemberService } from 'app/entities/team-staff-member/service/team-staff-member.service';

import { CallupUpdateComponent } from './callup-update.component';

describe('Callup Management Update Component', () => {
  let comp: CallupUpdateComponent;
  let fixture: ComponentFixture<CallupUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let callupService: CallupService;
  let matchPlayerService: MatchPlayerService;
  let teamStaffMemberService: TeamStaffMemberService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CallupUpdateComponent],
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
      .overrideTemplate(CallupUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CallupUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    callupService = TestBed.inject(CallupService);
    matchPlayerService = TestBed.inject(MatchPlayerService);
    teamStaffMemberService = TestBed.inject(TeamStaffMemberService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MatchPlayer query and add missing value', () => {
      const callup: ICallup = { id: 456 };
      const players: IMatchPlayer[] = [{ id: 7711 }];
      callup.players = players;

      const matchPlayerCollection: IMatchPlayer[] = [{ id: 62077 }];
      jest.spyOn(matchPlayerService, 'query').mockReturnValue(of(new HttpResponse({ body: matchPlayerCollection })));
      const additionalMatchPlayers = [...players];
      const expectedCollection: IMatchPlayer[] = [...additionalMatchPlayers, ...matchPlayerCollection];
      jest.spyOn(matchPlayerService, 'addMatchPlayerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ callup });
      comp.ngOnInit();

      expect(matchPlayerService.query).toHaveBeenCalled();
      expect(matchPlayerService.addMatchPlayerToCollectionIfMissing).toHaveBeenCalledWith(matchPlayerCollection, ...additionalMatchPlayers);
      expect(comp.matchPlayersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call captain query and add missing value', () => {
      const callup: ICallup = { id: 456 };
      const captain: IMatchPlayer = { id: 33605 };
      callup.captain = captain;

      const captainCollection: IMatchPlayer[] = [{ id: 18010 }];
      jest.spyOn(matchPlayerService, 'query').mockReturnValue(of(new HttpResponse({ body: captainCollection })));
      const expectedCollection: IMatchPlayer[] = [captain, ...captainCollection];
      jest.spyOn(matchPlayerService, 'addMatchPlayerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ callup });
      comp.ngOnInit();

      expect(matchPlayerService.query).toHaveBeenCalled();
      expect(matchPlayerService.addMatchPlayerToCollectionIfMissing).toHaveBeenCalledWith(captainCollection, captain);
      expect(comp.captainsCollection).toEqual(expectedCollection);
    });

    it('Should call TeamStaffMember query and add missing value', () => {
      const callup: ICallup = { id: 456 };
      const dt: ITeamStaffMember = { id: 12874 };
      callup.dt = dt;
      const dt2: ITeamStaffMember = { id: 21028 };
      callup.dt2 = dt2;
      const teamDelegate: ITeamStaffMember = { id: 22379 };
      callup.teamDelegate = teamDelegate;

      const teamStaffMemberCollection: ITeamStaffMember[] = [{ id: 52418 }];
      jest.spyOn(teamStaffMemberService, 'query').mockReturnValue(of(new HttpResponse({ body: teamStaffMemberCollection })));
      const additionalTeamStaffMembers = [dt, dt2, teamDelegate];
      const expectedCollection: ITeamStaffMember[] = [...additionalTeamStaffMembers, ...teamStaffMemberCollection];
      jest.spyOn(teamStaffMemberService, 'addTeamStaffMemberToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ callup });
      comp.ngOnInit();

      expect(teamStaffMemberService.query).toHaveBeenCalled();
      expect(teamStaffMemberService.addTeamStaffMemberToCollectionIfMissing).toHaveBeenCalledWith(
        teamStaffMemberCollection,
        ...additionalTeamStaffMembers
      );
      expect(comp.teamStaffMembersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const callup: ICallup = { id: 456 };
      const captain: IMatchPlayer = { id: 14595 };
      callup.captain = captain;
      const players: IMatchPlayer = { id: 56566 };
      callup.players = [players];
      const dt: ITeamStaffMember = { id: 16572 };
      callup.dt = dt;
      const dt2: ITeamStaffMember = { id: 85461 };
      callup.dt2 = dt2;
      const teamDelegate: ITeamStaffMember = { id: 78584 };
      callup.teamDelegate = teamDelegate;

      activatedRoute.data = of({ callup });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(callup));
      expect(comp.captainsCollection).toContain(captain);
      expect(comp.matchPlayersSharedCollection).toContain(players);
      expect(comp.teamStaffMembersSharedCollection).toContain(dt);
      expect(comp.teamStaffMembersSharedCollection).toContain(dt2);
      expect(comp.teamStaffMembersSharedCollection).toContain(teamDelegate);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Callup>>();
      const callup = { id: 123 };
      jest.spyOn(callupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ callup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: callup }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(callupService.update).toHaveBeenCalledWith(callup);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Callup>>();
      const callup = new Callup();
      jest.spyOn(callupService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ callup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: callup }));
      saveSubject.complete();

      // THEN
      expect(callupService.create).toHaveBeenCalledWith(callup);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Callup>>();
      const callup = { id: 123 };
      jest.spyOn(callupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ callup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(callupService.update).toHaveBeenCalledWith(callup);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMatchPlayerById', () => {
      it('Should return tracked MatchPlayer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMatchPlayerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTeamStaffMemberById', () => {
      it('Should return tracked TeamStaffMember primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTeamStaffMemberById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedMatchPlayer', () => {
      it('Should return option if no MatchPlayer is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedMatchPlayer(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected MatchPlayer for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedMatchPlayer(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this MatchPlayer is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedMatchPlayer(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
