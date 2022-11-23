import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MatchPlayerService } from '../service/match-player.service';
import { IMatchPlayer, MatchPlayer } from '../match-player.model';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { IPosition } from 'app/entities/position/position.model';
import { PositionService } from 'app/entities/position/service/position.service';

import { MatchPlayerUpdateComponent } from './match-player-update.component';

describe('MatchPlayer Management Update Component', () => {
  let comp: MatchPlayerUpdateComponent;
  let fixture: ComponentFixture<MatchPlayerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let matchPlayerService: MatchPlayerService;
  let playerService: PlayerService;
  let positionService: PositionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MatchPlayerUpdateComponent],
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
      .overrideTemplate(MatchPlayerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MatchPlayerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    matchPlayerService = TestBed.inject(MatchPlayerService);
    playerService = TestBed.inject(PlayerService);
    positionService = TestBed.inject(PositionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Player query and add missing value', () => {
      const matchPlayer: IMatchPlayer = { id: 456 };
      const player: IPlayer = { id: 48590 };
      matchPlayer.player = player;

      const playerCollection: IPlayer[] = [{ id: 56895 }];
      jest.spyOn(playerService, 'query').mockReturnValue(of(new HttpResponse({ body: playerCollection })));
      const additionalPlayers = [player];
      const expectedCollection: IPlayer[] = [...additionalPlayers, ...playerCollection];
      jest.spyOn(playerService, 'addPlayerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ matchPlayer });
      comp.ngOnInit();

      expect(playerService.query).toHaveBeenCalled();
      expect(playerService.addPlayerToCollectionIfMissing).toHaveBeenCalledWith(playerCollection, ...additionalPlayers);
      expect(comp.playersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Position query and add missing value', () => {
      const matchPlayer: IMatchPlayer = { id: 456 };
      const position: IPosition = { id: 5937 };
      matchPlayer.position = position;

      const positionCollection: IPosition[] = [{ id: 1064 }];
      jest.spyOn(positionService, 'query').mockReturnValue(of(new HttpResponse({ body: positionCollection })));
      const additionalPositions = [position];
      const expectedCollection: IPosition[] = [...additionalPositions, ...positionCollection];
      jest.spyOn(positionService, 'addPositionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ matchPlayer });
      comp.ngOnInit();

      expect(positionService.query).toHaveBeenCalled();
      expect(positionService.addPositionToCollectionIfMissing).toHaveBeenCalledWith(positionCollection, ...additionalPositions);
      expect(comp.positionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const matchPlayer: IMatchPlayer = { id: 456 };
      const player: IPlayer = { id: 73667 };
      matchPlayer.player = player;
      const position: IPosition = { id: 68712 };
      matchPlayer.position = position;

      activatedRoute.data = of({ matchPlayer });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(matchPlayer));
      expect(comp.playersSharedCollection).toContain(player);
      expect(comp.positionsSharedCollection).toContain(position);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MatchPlayer>>();
      const matchPlayer = { id: 123 };
      jest.spyOn(matchPlayerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matchPlayer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: matchPlayer }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(matchPlayerService.update).toHaveBeenCalledWith(matchPlayer);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MatchPlayer>>();
      const matchPlayer = new MatchPlayer();
      jest.spyOn(matchPlayerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matchPlayer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: matchPlayer }));
      saveSubject.complete();

      // THEN
      expect(matchPlayerService.create).toHaveBeenCalledWith(matchPlayer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MatchPlayer>>();
      const matchPlayer = { id: 123 };
      jest.spyOn(matchPlayerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matchPlayer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(matchPlayerService.update).toHaveBeenCalledWith(matchPlayer);
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

    describe('trackPositionById', () => {
      it('Should return tracked Position primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPositionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
