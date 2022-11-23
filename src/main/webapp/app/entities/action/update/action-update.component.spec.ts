import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ActionService } from '../service/action.service';
import { IAction, Action } from '../action.model';
import { IMatchStats } from 'app/entities/match-stats/match-stats.model';
import { MatchStatsService } from 'app/entities/match-stats/service/match-stats.service';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';

import { ActionUpdateComponent } from './action-update.component';

describe('Action Management Update Component', () => {
  let comp: ActionUpdateComponent;
  let fixture: ComponentFixture<ActionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let actionService: ActionService;
  let matchStatsService: MatchStatsService;
  let playerService: PlayerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ActionUpdateComponent],
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
      .overrideTemplate(ActionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ActionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    actionService = TestBed.inject(ActionService);
    matchStatsService = TestBed.inject(MatchStatsService);
    playerService = TestBed.inject(PlayerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MatchStats query and add missing value', () => {
      const action: IAction = { id: 456 };
      const matchStats: IMatchStats = { id: 39000 };
      action.matchStats = matchStats;

      const matchStatsCollection: IMatchStats[] = [{ id: 4868 }];
      jest.spyOn(matchStatsService, 'query').mockReturnValue(of(new HttpResponse({ body: matchStatsCollection })));
      const additionalMatchStats = [matchStats];
      const expectedCollection: IMatchStats[] = [...additionalMatchStats, ...matchStatsCollection];
      jest.spyOn(matchStatsService, 'addMatchStatsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ action });
      comp.ngOnInit();

      expect(matchStatsService.query).toHaveBeenCalled();
      expect(matchStatsService.addMatchStatsToCollectionIfMissing).toHaveBeenCalledWith(matchStatsCollection, ...additionalMatchStats);
      expect(comp.matchStatsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Player query and add missing value', () => {
      const action: IAction = { id: 456 };
      const players: IPlayer[] = [{ id: 77418 }];
      action.players = players;

      const playerCollection: IPlayer[] = [{ id: 31134 }];
      jest.spyOn(playerService, 'query').mockReturnValue(of(new HttpResponse({ body: playerCollection })));
      const additionalPlayers = [...players];
      const expectedCollection: IPlayer[] = [...additionalPlayers, ...playerCollection];
      jest.spyOn(playerService, 'addPlayerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ action });
      comp.ngOnInit();

      expect(playerService.query).toHaveBeenCalled();
      expect(playerService.addPlayerToCollectionIfMissing).toHaveBeenCalledWith(playerCollection, ...additionalPlayers);
      expect(comp.playersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const action: IAction = { id: 456 };
      const matchStats: IMatchStats = { id: 33149 };
      action.matchStats = matchStats;
      const players: IPlayer = { id: 5032 };
      action.players = [players];

      activatedRoute.data = of({ action });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(action));
      expect(comp.matchStatsSharedCollection).toContain(matchStats);
      expect(comp.playersSharedCollection).toContain(players);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Action>>();
      const action = { id: 123 };
      jest.spyOn(actionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ action });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: action }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(actionService.update).toHaveBeenCalledWith(action);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Action>>();
      const action = new Action();
      jest.spyOn(actionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ action });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: action }));
      saveSubject.complete();

      // THEN
      expect(actionService.create).toHaveBeenCalledWith(action);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Action>>();
      const action = { id: 123 };
      jest.spyOn(actionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ action });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(actionService.update).toHaveBeenCalledWith(action);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMatchStatsById', () => {
      it('Should return tracked MatchStats primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMatchStatsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPlayerById', () => {
      it('Should return tracked Player primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlayerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedPlayer', () => {
      it('Should return option if no Player is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPlayer(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Player for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPlayer(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Player is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPlayer(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
