import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LineupService } from '../service/lineup.service';
import { ILineup, Lineup } from '../lineup.model';
import { ICallup } from 'app/entities/callup/callup.model';
import { CallupService } from 'app/entities/callup/service/callup.service';
import { IFormation } from 'app/entities/formation/formation.model';
import { FormationService } from 'app/entities/formation/service/formation.service';
import { IMatchPlayer } from 'app/entities/match-player/match-player.model';
import { MatchPlayerService } from 'app/entities/match-player/service/match-player.service';

import { LineupUpdateComponent } from './lineup-update.component';

describe('Lineup Management Update Component', () => {
  let comp: LineupUpdateComponent;
  let fixture: ComponentFixture<LineupUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let lineupService: LineupService;
  let callupService: CallupService;
  let formationService: FormationService;
  let matchPlayerService: MatchPlayerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LineupUpdateComponent],
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
      .overrideTemplate(LineupUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LineupUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    lineupService = TestBed.inject(LineupService);
    callupService = TestBed.inject(CallupService);
    formationService = TestBed.inject(FormationService);
    matchPlayerService = TestBed.inject(MatchPlayerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call callup query and add missing value', () => {
      const lineup: ILineup = { id: 456 };
      const callup: ICallup = { id: 15773 };
      lineup.callup = callup;

      const callupCollection: ICallup[] = [{ id: 93048 }];
      jest.spyOn(callupService, 'query').mockReturnValue(of(new HttpResponse({ body: callupCollection })));
      const expectedCollection: ICallup[] = [callup, ...callupCollection];
      jest.spyOn(callupService, 'addCallupToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ lineup });
      comp.ngOnInit();

      expect(callupService.query).toHaveBeenCalled();
      expect(callupService.addCallupToCollectionIfMissing).toHaveBeenCalledWith(callupCollection, callup);
      expect(comp.callupsCollection).toEqual(expectedCollection);
    });

    it('Should call Formation query and add missing value', () => {
      const lineup: ILineup = { id: 456 };
      const formation: IFormation = { id: 21405 };
      lineup.formation = formation;

      const formationCollection: IFormation[] = [{ id: 24807 }];
      jest.spyOn(formationService, 'query').mockReturnValue(of(new HttpResponse({ body: formationCollection })));
      const additionalFormations = [formation];
      const expectedCollection: IFormation[] = [...additionalFormations, ...formationCollection];
      jest.spyOn(formationService, 'addFormationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ lineup });
      comp.ngOnInit();

      expect(formationService.query).toHaveBeenCalled();
      expect(formationService.addFormationToCollectionIfMissing).toHaveBeenCalledWith(formationCollection, ...additionalFormations);
      expect(comp.formationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call MatchPlayer query and add missing value', () => {
      const lineup: ILineup = { id: 456 };
      const players: IMatchPlayer[] = [{ id: 20342 }];
      lineup.players = players;

      const matchPlayerCollection: IMatchPlayer[] = [{ id: 35014 }];
      jest.spyOn(matchPlayerService, 'query').mockReturnValue(of(new HttpResponse({ body: matchPlayerCollection })));
      const additionalMatchPlayers = [...players];
      const expectedCollection: IMatchPlayer[] = [...additionalMatchPlayers, ...matchPlayerCollection];
      jest.spyOn(matchPlayerService, 'addMatchPlayerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ lineup });
      comp.ngOnInit();

      expect(matchPlayerService.query).toHaveBeenCalled();
      expect(matchPlayerService.addMatchPlayerToCollectionIfMissing).toHaveBeenCalledWith(matchPlayerCollection, ...additionalMatchPlayers);
      expect(comp.matchPlayersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const lineup: ILineup = { id: 456 };
      const callup: ICallup = { id: 86769 };
      lineup.callup = callup;
      const formation: IFormation = { id: 92626 };
      lineup.formation = formation;
      const players: IMatchPlayer = { id: 20874 };
      lineup.players = [players];

      activatedRoute.data = of({ lineup });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(lineup));
      expect(comp.callupsCollection).toContain(callup);
      expect(comp.formationsSharedCollection).toContain(formation);
      expect(comp.matchPlayersSharedCollection).toContain(players);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Lineup>>();
      const lineup = { id: 123 };
      jest.spyOn(lineupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lineup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lineup }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(lineupService.update).toHaveBeenCalledWith(lineup);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Lineup>>();
      const lineup = new Lineup();
      jest.spyOn(lineupService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lineup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lineup }));
      saveSubject.complete();

      // THEN
      expect(lineupService.create).toHaveBeenCalledWith(lineup);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Lineup>>();
      const lineup = { id: 123 };
      jest.spyOn(lineupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lineup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(lineupService.update).toHaveBeenCalledWith(lineup);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCallupById', () => {
      it('Should return tracked Callup primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCallupById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFormationById', () => {
      it('Should return tracked Formation primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFormationById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackMatchPlayerById', () => {
      it('Should return tracked MatchPlayer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMatchPlayerById(0, entity);
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
