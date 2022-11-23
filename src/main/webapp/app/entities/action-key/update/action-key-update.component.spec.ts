import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ActionKeyService } from '../service/action-key.service';
import { IActionKey, ActionKey } from '../action-key.model';

import { ActionKeyUpdateComponent } from './action-key-update.component';

describe('ActionKey Management Update Component', () => {
  let comp: ActionKeyUpdateComponent;
  let fixture: ComponentFixture<ActionKeyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let actionKeyService: ActionKeyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ActionKeyUpdateComponent],
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
      .overrideTemplate(ActionKeyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ActionKeyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    actionKeyService = TestBed.inject(ActionKeyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const actionKey: IActionKey = { id: 456 };

      activatedRoute.data = of({ actionKey });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(actionKey));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ActionKey>>();
      const actionKey = { id: 123 };
      jest.spyOn(actionKeyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ actionKey });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: actionKey }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(actionKeyService.update).toHaveBeenCalledWith(actionKey);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ActionKey>>();
      const actionKey = new ActionKey();
      jest.spyOn(actionKeyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ actionKey });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: actionKey }));
      saveSubject.complete();

      // THEN
      expect(actionKeyService.create).toHaveBeenCalledWith(actionKey);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ActionKey>>();
      const actionKey = { id: 123 };
      jest.spyOn(actionKeyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ actionKey });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(actionKeyService.update).toHaveBeenCalledWith(actionKey);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
