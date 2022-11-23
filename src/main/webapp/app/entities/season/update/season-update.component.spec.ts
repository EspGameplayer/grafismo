import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SeasonService } from '../service/season.service';
import { ISeason, Season } from '../season.model';

import { SeasonUpdateComponent } from './season-update.component';

describe('Season Management Update Component', () => {
  let comp: SeasonUpdateComponent;
  let fixture: ComponentFixture<SeasonUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let seasonService: SeasonService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SeasonUpdateComponent],
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
      .overrideTemplate(SeasonUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SeasonUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    seasonService = TestBed.inject(SeasonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const season: ISeason = { id: 456 };

      activatedRoute.data = of({ season });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(season));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Season>>();
      const season = { id: 123 };
      jest.spyOn(seasonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ season });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: season }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(seasonService.update).toHaveBeenCalledWith(season);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Season>>();
      const season = new Season();
      jest.spyOn(seasonService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ season });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: season }));
      saveSubject.complete();

      // THEN
      expect(seasonService.create).toHaveBeenCalledWith(season);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Season>>();
      const season = { id: 123 };
      jest.spyOn(seasonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ season });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(seasonService.update).toHaveBeenCalledWith(season);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
