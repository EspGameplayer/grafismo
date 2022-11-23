import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CompetitionPlayerService } from '../service/competition-player.service';
import { ICompetitionPlayer, CompetitionPlayer } from '../competition-player.model';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { ICompetition } from 'app/entities/competition/competition.model';
import { CompetitionService } from 'app/entities/competition/service/competition.service';
import { IPosition } from 'app/entities/position/position.model';
import { PositionService } from 'app/entities/position/service/position.service';

import { CompetitionPlayerUpdateComponent } from './competition-player-update.component';

describe('CompetitionPlayer Management Update Component', () => {
  let comp: CompetitionPlayerUpdateComponent;
  let fixture: ComponentFixture<CompetitionPlayerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let competitionPlayerService: CompetitionPlayerService;
  let playerService: PlayerService;
  let competitionService: CompetitionService;
  let positionService: PositionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CompetitionPlayerUpdateComponent],
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
      .overrideTemplate(CompetitionPlayerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompetitionPlayerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    competitionPlayerService = TestBed.inject(CompetitionPlayerService);
    playerService = TestBed.inject(PlayerService);
    competitionService = TestBed.inject(CompetitionService);
    positionService = TestBed.inject(PositionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Player query and add missing value', () => {
      const competitionPlayer: ICompetitionPlayer = { id: 456 };
      const player: IPlayer = { id: 22857 };
      competitionPlayer.player = player;

      const playerCollection: IPlayer[] = [{ id: 39591 }];
      jest.spyOn(playerService, 'query').mockReturnValue(of(new HttpResponse({ body: playerCollection })));
      const additionalPlayers = [player];
      const expectedCollection: IPlayer[] = [...additionalPlayers, ...playerCollection];
      jest.spyOn(playerService, 'addPlayerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ competitionPlayer });
      comp.ngOnInit();

      expect(playerService.query).toHaveBeenCalled();
      expect(playerService.addPlayerToCollectionIfMissing).toHaveBeenCalledWith(playerCollection, ...additionalPlayers);
      expect(comp.playersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Competition query and add missing value', () => {
      const competitionPlayer: ICompetitionPlayer = { id: 456 };
      const competition: ICompetition = { id: 86001 };
      competitionPlayer.competition = competition;

      const competitionCollection: ICompetition[] = [{ id: 20252 }];
      jest.spyOn(competitionService, 'query').mockReturnValue(of(new HttpResponse({ body: competitionCollection })));
      const additionalCompetitions = [competition];
      const expectedCollection: ICompetition[] = [...additionalCompetitions, ...competitionCollection];
      jest.spyOn(competitionService, 'addCompetitionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ competitionPlayer });
      comp.ngOnInit();

      expect(competitionService.query).toHaveBeenCalled();
      expect(competitionService.addCompetitionToCollectionIfMissing).toHaveBeenCalledWith(competitionCollection, ...additionalCompetitions);
      expect(comp.competitionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Position query and add missing value', () => {
      const competitionPlayer: ICompetitionPlayer = { id: 456 };
      const preferredPositions: IPosition[] = [{ id: 58319 }];
      competitionPlayer.preferredPositions = preferredPositions;

      const positionCollection: IPosition[] = [{ id: 29792 }];
      jest.spyOn(positionService, 'query').mockReturnValue(of(new HttpResponse({ body: positionCollection })));
      const additionalPositions = [...preferredPositions];
      const expectedCollection: IPosition[] = [...additionalPositions, ...positionCollection];
      jest.spyOn(positionService, 'addPositionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ competitionPlayer });
      comp.ngOnInit();

      expect(positionService.query).toHaveBeenCalled();
      expect(positionService.addPositionToCollectionIfMissing).toHaveBeenCalledWith(positionCollection, ...additionalPositions);
      expect(comp.positionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const competitionPlayer: ICompetitionPlayer = { id: 456 };
      const player: IPlayer = { id: 76659 };
      competitionPlayer.player = player;
      const competition: ICompetition = { id: 39914 };
      competitionPlayer.competition = competition;
      const preferredPositions: IPosition = { id: 92988 };
      competitionPlayer.preferredPositions = [preferredPositions];

      activatedRoute.data = of({ competitionPlayer });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(competitionPlayer));
      expect(comp.playersSharedCollection).toContain(player);
      expect(comp.competitionsSharedCollection).toContain(competition);
      expect(comp.positionsSharedCollection).toContain(preferredPositions);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CompetitionPlayer>>();
      const competitionPlayer = { id: 123 };
      jest.spyOn(competitionPlayerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ competitionPlayer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: competitionPlayer }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(competitionPlayerService.update).toHaveBeenCalledWith(competitionPlayer);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CompetitionPlayer>>();
      const competitionPlayer = new CompetitionPlayer();
      jest.spyOn(competitionPlayerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ competitionPlayer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: competitionPlayer }));
      saveSubject.complete();

      // THEN
      expect(competitionPlayerService.create).toHaveBeenCalledWith(competitionPlayer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CompetitionPlayer>>();
      const competitionPlayer = { id: 123 };
      jest.spyOn(competitionPlayerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ competitionPlayer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(competitionPlayerService.update).toHaveBeenCalledWith(competitionPlayer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPlayerById', () => {
      it('Should return tracked Player primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlayerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCompetitionById', () => {
      it('Should return tracked Competition primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCompetitionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPositionById', () => {
      it('Should return tracked Position primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPositionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedPosition', () => {
      it('Should return option if no Position is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPosition(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Position for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPosition(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Position is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPosition(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
