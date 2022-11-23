import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PositionService } from '../service/position.service';
import { IPosition, Position } from '../position.model';

import { PositionUpdateComponent } from './position-update.component';

describe('Position Management Update Component', () => {
  let comp: PositionUpdateComponent;
  let fixture: ComponentFixture<PositionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let positionService: PositionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PositionUpdateComponent],
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
      .overrideTemplate(PositionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PositionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    positionService = TestBed.inject(PositionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Position query and add missing value', () => {
      const position: IPosition = { id: 456 };
      const parents: IPosition[] = [{ id: 53010 }];
      position.parents = parents;

      const positionCollection: IPosition[] = [{ id: 32901 }];
      jest.spyOn(positionService, 'query').mockReturnValue(of(new HttpResponse({ body: positionCollection })));
      const additionalPositions = [...parents];
      const expectedCollection: IPosition[] = [...additionalPositions, ...positionCollection];
      jest.spyOn(positionService, 'addPositionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ position });
      comp.ngOnInit();

      expect(positionService.query).toHaveBeenCalled();
      expect(positionService.addPositionToCollectionIfMissing).toHaveBeenCalledWith(positionCollection, ...additionalPositions);
      expect(comp.positionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const position: IPosition = { id: 456 };
      const parents: IPosition = { id: 36968 };
      position.parents = [parents];

      activatedRoute.data = of({ position });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(position));
      expect(comp.positionsSharedCollection).toContain(parents);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Position>>();
      const position = { id: 123 };
      jest.spyOn(positionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ position });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: position }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(positionService.update).toHaveBeenCalledWith(position);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Position>>();
      const position = new Position();
      jest.spyOn(positionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ position });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: position }));
      saveSubject.complete();

      // THEN
      expect(positionService.create).toHaveBeenCalledWith(position);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Position>>();
      const position = { id: 123 };
      jest.spyOn(positionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ position });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(positionService.update).toHaveBeenCalledWith(position);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
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
