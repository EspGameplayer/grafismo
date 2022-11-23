import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GraphicElementPosService } from '../service/graphic-element-pos.service';
import { IGraphicElementPos, GraphicElementPos } from '../graphic-element-pos.model';

import { GraphicElementPosUpdateComponent } from './graphic-element-pos-update.component';

describe('GraphicElementPos Management Update Component', () => {
  let comp: GraphicElementPosUpdateComponent;
  let fixture: ComponentFixture<GraphicElementPosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let graphicElementPosService: GraphicElementPosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GraphicElementPosUpdateComponent],
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
      .overrideTemplate(GraphicElementPosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GraphicElementPosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    graphicElementPosService = TestBed.inject(GraphicElementPosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call GraphicElementPos query and add missing value', () => {
      const graphicElementPos: IGraphicElementPos = { id: 456 };
      const parent: IGraphicElementPos = { id: 54394 };
      graphicElementPos.parent = parent;

      const graphicElementPosCollection: IGraphicElementPos[] = [{ id: 29621 }];
      jest.spyOn(graphicElementPosService, 'query').mockReturnValue(of(new HttpResponse({ body: graphicElementPosCollection })));
      const additionalGraphicElementPos = [parent];
      const expectedCollection: IGraphicElementPos[] = [...additionalGraphicElementPos, ...graphicElementPosCollection];
      jest.spyOn(graphicElementPosService, 'addGraphicElementPosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ graphicElementPos });
      comp.ngOnInit();

      expect(graphicElementPosService.query).toHaveBeenCalled();
      expect(graphicElementPosService.addGraphicElementPosToCollectionIfMissing).toHaveBeenCalledWith(
        graphicElementPosCollection,
        ...additionalGraphicElementPos
      );
      expect(comp.graphicElementPosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const graphicElementPos: IGraphicElementPos = { id: 456 };
      const parent: IGraphicElementPos = { id: 11893 };
      graphicElementPos.parent = parent;

      activatedRoute.data = of({ graphicElementPos });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(graphicElementPos));
      expect(comp.graphicElementPosSharedCollection).toContain(parent);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GraphicElementPos>>();
      const graphicElementPos = { id: 123 };
      jest.spyOn(graphicElementPosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ graphicElementPos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: graphicElementPos }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(graphicElementPosService.update).toHaveBeenCalledWith(graphicElementPos);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GraphicElementPos>>();
      const graphicElementPos = new GraphicElementPos();
      jest.spyOn(graphicElementPosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ graphicElementPos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: graphicElementPos }));
      saveSubject.complete();

      // THEN
      expect(graphicElementPosService.create).toHaveBeenCalledWith(graphicElementPos);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GraphicElementPos>>();
      const graphicElementPos = { id: 123 };
      jest.spyOn(graphicElementPosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ graphicElementPos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(graphicElementPosService.update).toHaveBeenCalledWith(graphicElementPos);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackGraphicElementPosById', () => {
      it('Should return tracked GraphicElementPos primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackGraphicElementPosById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
