import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InjuryService } from '../service/injury.service';
import { IInjury, Injury } from '../injury.model';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';

import { InjuryUpdateComponent } from './injury-update.component';

describe('Injury Management Update Component', () => {
  let comp: InjuryUpdateComponent;
  let fixture: ComponentFixture<InjuryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let injuryService: InjuryService;
  let playerService: PlayerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InjuryUpdateComponent],
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
      .overrideTemplate(InjuryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InjuryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    injuryService = TestBed.inject(InjuryService);
    playerService = TestBed.inject(PlayerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Player query and add missing value', () => {
      const injury: IInjury = { id: 456 };
      const player: IPlayer = { id: 43109 };
      injury.player = player;

      const playerCollection: IPlayer[] = [{ id: 78995 }];
      jest.spyOn(playerService, 'query').mockReturnValue(of(new HttpResponse({ body: playerCollection })));
      const additionalPlayers = [player];
      const expectedCollection: IPlayer[] = [...additionalPlayers, ...playerCollection];
      jest.spyOn(playerService, 'addPlayerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ injury });
      comp.ngOnInit();

      expect(playerService.query).toHaveBeenCalled();
      expect(playerService.addPlayerToCollectionIfMissing).toHaveBeenCalledWith(playerCollection, ...additionalPlayers);
      expect(comp.playersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const injury: IInjury = { id: 456 };
      const player: IPlayer = { id: 32232 };
      injury.player = player;

      activatedRoute.data = of({ injury });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(injury));
      expect(comp.playersSharedCollection).toContain(player);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Injury>>();
      const injury = { id: 123 };
      jest.spyOn(injuryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ injury });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: injury }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(injuryService.update).toHaveBeenCalledWith(injury);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Injury>>();
      const injury = new Injury();
      jest.spyOn(injuryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ injury });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: injury }));
      saveSubject.complete();

      // THEN
      expect(injuryService.create).toHaveBeenCalledWith(injury);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Injury>>();
      const injury = { id: 123 };
      jest.spyOn(injuryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ injury });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(injuryService.update).toHaveBeenCalledWith(injury);
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
  });
});
