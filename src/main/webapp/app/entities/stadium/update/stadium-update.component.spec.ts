import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { StadiumService } from '../service/stadium.service';
import { IStadium, Stadium } from '../stadium.model';

import { StadiumUpdateComponent } from './stadium-update.component';

describe('Stadium Management Update Component', () => {
  let comp: StadiumUpdateComponent;
  let fixture: ComponentFixture<StadiumUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let stadiumService: StadiumService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [StadiumUpdateComponent],
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
      .overrideTemplate(StadiumUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StadiumUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    stadiumService = TestBed.inject(StadiumService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const stadium: IStadium = { id: 456 };

      activatedRoute.data = of({ stadium });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(stadium));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Stadium>>();
      const stadium = { id: 123 };
      jest.spyOn(stadiumService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stadium });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stadium }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(stadiumService.update).toHaveBeenCalledWith(stadium);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Stadium>>();
      const stadium = new Stadium();
      jest.spyOn(stadiumService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stadium });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stadium }));
      saveSubject.complete();

      // THEN
      expect(stadiumService.create).toHaveBeenCalledWith(stadium);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Stadium>>();
      const stadium = { id: 123 };
      jest.spyOn(stadiumService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stadium });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(stadiumService.update).toHaveBeenCalledWith(stadium);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
